$(document).ready(function () {
    $('#login-form').submit(function (e) {
        e.preventDefault(); // Предотвращаем отправку формы по умолчанию

        var formData = {
            username: $('#username').val(), // Получаем значение поля с именем пользователя
            password: $('#password').val() // Получаем значение поля с паролем
        };

        $.ajax({
            type: 'POST',
            url: '/login', // URL для обработки аутентификации
            data: formData,
            success: function (response) {
                // Обработка успешной аутентификации
                console.log('Успешная аутентификация');
                window.location.href = '/index'; // Перенаправляем на главную страницу после входа
            },
            error: function (xhr, status, error) {
                // Обработка ошибок аутентификации
                console.error('Ошибка аутентификации:', error);
                $('#errorMessage').text('Ошибка аутентификации, пожалуйста, попробуйте еще раз.');
            }
        });
    });
});
