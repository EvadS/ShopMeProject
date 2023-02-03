package com.shopme.admin.user;


import com.shopme.admin.user.common.entity.Role;
import com.shopme.admin.user.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public  static  final  int USERS_PER_PAGE = 4;

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {

            final User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    private void encodePassword(User user) {
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
    }

    // TODO: check exists by
    public boolean isEmailUnique(Integer id, String email) {
        User userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail == null) {
            return true;
        }

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            return userByEmail == null;
        } else {
            return userByEmail.getId() == id;
        }
    }

    public User get(Integer id) {
        try {
            return userRepository.findById(id).get();

        } catch (NoSuchElementException ex) {
            throw new ResourceNotFoundException("Could not find any user with ID:" + id);
        }
    }

    public void delete(Integer id) {
        final Long countById = userRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new UsernameNotFoundException("Could not find user by id:" + id);
        }

        userRepository.deleteById(id);
    }

    public void updateUsed(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending():sort.descending();

        Pageable pageable = PageRequest.of(pageNum-1, USERS_PER_PAGE,sort );
        return  userRepository.findAll( pageable);
    }

}
