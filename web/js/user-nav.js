document.addEventListener('DOMContentLoaded', function() {
    const userNavBtn = document.getElementById('userNavBtn');
    const userNameDisplay = document.getElementById('userNameDisplay');
    const userDropdown = userNavBtn?.querySelector('.user-dropdown');
    let dropdownTimeout;

    // Проверка авторизации
    function isAuthenticated() {
        return localStorage.getItem('isAuthenticated') === 'true';
    }

    // Получение данных пользователя
    function getUserData() {
        return {
            name: localStorage.getItem('userName') || 'Пользователь',
            role: localStorage.getItem('userRole') || 'patient'
        };
    }

    // Выход из системы
    function logout(e) {
        e.preventDefault();
        if (confirm('Вы уверены, что хотите выйти?')) {
            localStorage.removeItem('isAuthenticated');
            localStorage.removeItem('userName');
            localStorage.removeItem('userRole');
            window.location.href = 'index.html';
        }
    }

    // Генерация меню
    function generateMenu(role) {
        let menuItems = `
            <a href="${role === 'doctor' ? 'vrach.html' : 'personal-account.html'}" class="dropdown-item">
                ${role === 'doctor' ? 'Рабочий кабинет' : 'Личный кабинет'}
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item logout">Выйти</a>
        `;
        return menuItems;
    }

    // Обновление отображения
    function updateUserDisplay() {
        if (!userNavBtn) return;

        if (isAuthenticated()) {
            const user = getUserData();
            userNameDisplay.textContent = user.name.split(' ').slice(0, 2).join(' '); // Показываем только имя и фамилию
            
            if (userDropdown) {
                userDropdown.innerHTML = generateMenu(user.role);
                userDropdown.querySelector('.logout').addEventListener('click', logout);
            }

            // Для авторизованных - стандартное поведение
            userNavBtn.onclick = null;
            userNavBtn.style.cursor = 'pointer';
        } else {
            userNameDisplay.textContent = 'Войти';
            userNavBtn.onclick = () => window.location.href = 'pacient.html?login=true';
            userNavBtn.style.cursor = 'pointer';
        }
    }

    // Управление выпадающим меню
    function setupDropdown() {
        if (!userNavBtn || !userDropdown || !isAuthenticated()) return;

        // Показать меню
        const showDropdown = () => {
            clearTimeout(dropdownTimeout);
            userDropdown.classList.add('show');
        };

        // Скрыть меню с задержкой
        const hideDropdown = () => {
            dropdownTimeout = setTimeout(() => {
                userDropdown.classList.remove('show');
            }, 200);
        };

        // Отменить скрытие
        const cancelHide = () => {
            clearTimeout(dropdownTimeout);
        };

        // Десктопные события
        userNavBtn.addEventListener('mouseenter', showDropdown);
        userNavBtn.addEventListener('mouseleave', hideDropdown);
        userDropdown.addEventListener('mouseenter', cancelHide);
        userDropdown.addEventListener('mouseleave', hideDropdown);

        // Мобильные события
        userNavBtn.addEventListener('touchstart', (e) => {
            e.preventDefault();
            if (userDropdown.classList.contains('show')) {
                hideDropdown();
            } else {
                showDropdown();
            }
        });

        // Закрытие при клике вне меню
        document.addEventListener('click', (e) => {
            if (!userNavBtn.contains(e.target)) {
                hideDropdown();
            }
        });
    }

    // Инициализация
    function init() {
        updateUserDisplay();
        setupDropdown();
        
        // Обновляем при изменениях в localStorage (если вкладки открыты)
        window.addEventListener('storage', updateUserDisplay);
    }

    init();
});