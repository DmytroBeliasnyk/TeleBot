$(document).ready(function () {
    $('#login-form').submit(function (e) {
        e.preventDefault();

        var formData = {
            username: $('#username').val(),
            password: $('#password').val()
        };

        $.ajax({
            type: 'POST',
            url: '/login',
            data: formData,
            success: function (response) {
                console.log('Success');
                window.location.href = '/admin';
            },
            error: function (xhr, status, error) {
                console.error('Invalid login or password:', error);
                $('#errorMessage').text('Invalid login or password');
            }
        });
    });
});
