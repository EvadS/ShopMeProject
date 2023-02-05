$(document).ready(function () {
    $("#logoutLink").on("click", function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    })

    customizeDropDownMenu();
});


function customizeDropDownMenu(){

        $(".navbar .dropdown").hover(
            function (){
                $(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();
            },
            function (){
                $(this).find('.dropdown-menu').first().stop(true, true).delay(1000).slideUp()

            }
        );
    $(".dropdown > a").click(function(){
        location.href = this.href;
    });
}

function checkPasswordMath(confirmPassword){
    if(confirmPassword.value != $("#password").val()){
        confirmPassword.setCustomValidity("Password do not match!");
    }else{
        confirmPassword.setCustomValidity("");
    }
}