document.addEventListener('DOMContentLoaded', function() {
    
    const transferBtns = document.querySelectorAll('.transfer-btn');
    const cancelBtns = document.querySelectorAll('.cancel-btn');
    const confirmBtns = document.querySelectorAll('.confirm-btn');
    const editBtn = document.querySelector('.edit-btn');
    const changePwdBtn = document.querySelector('.change-pwd-btn');
    const logoutBtn = document.querySelector('.logout-btn');

    transferBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            alert('Функция переноса приёма будет реализована позже');
        });
    });

    cancelBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            if(confirm('Вы уверены, что хотите отменить приём?')) {
                alert('Приём отменён');
            }
        });
    });

    confirmBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            alert('Приём подтверждён');
        });
    });

    editBtn.addEventListener('click', function() {
        alert('Редактирование данных будет доступно позже');
    });

    changePwdBtn.addEventListener('click', function() {
        alert('Форма смены пароля будет открыта');
    });

    logoutBtn.addEventListener('click', function() {
        if(confirm('Вы уверены, что хотите выйти?')) {
            window.location.href = 'index.html';
        }
    });
});