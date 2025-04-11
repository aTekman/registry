const loginBtn = document.getElementById('loginBtn');
const registerBtn = document.getElementById('registerBtn');
const actionLoginBtn = document.getElementById('actionLoginBtn');
const actionRegisterBtn = document.getElementById('actionRegisterBtn');
const closeModalBtn = document.getElementById('closeModalBtn');
const modal = document.getElementById('authModal');
const submitBtn = document.querySelector('.submit-btn');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

// Функции для работы с аутентификацией
function setAuthStatus(isAuthenticated, userData = {}) {
    localStorage.setItem('isAuthenticated', isAuthenticated);
    if (isAuthenticated && userData) {
        localStorage.setItem('userName', userData.name || 'Пользователь');
        localStorage.setItem('userRole', userData.role || 'patient');
    }
}

function clearAuthStatus() {
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userName');
    localStorage.removeItem('userRole');
}

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
}

function validatePassword(password) {
    return password.length >= 6;
}

// Работа с модальным окном
function openModal() {
    modal.style.display = 'block';
    document.body.style.overflow = 'hidden'; // Блокируем скролл страницы
}

function closeModal() {
    modal.style.display = 'none';
    document.body.style.overflow = ''; // Восстанавливаем скролл
    emailInput.value = '';
    passwordInput.value = '';
}

// Обработчик отправки формы
async function handleSubmit(e) {
    e.preventDefault();
    
    const email = emailInput.value.trim();
    const password = passwordInput.value.trim();
    
    // Валидация полей
    if (!validateEmail(email)) {
        alert('Пожалуйста, введите корректный email');
        return;
    }
    
    if (!validatePassword(password)) {
        alert('Пароль должен содержать минимум 6 символов');
        return;
    }
    
    try {
        // Здесь должна быть реальная проверка с сервером
        // Временная заглушка для демонстрации
        const users = [
            { 
                email: 'doctor@clinic.ru', 
                password: 'doctor123', 
                name: 'Иванов И. И.', 
                role: 'doctor' 
            },
            { 
                email: 'patient@clinic.ru', 
                password: 'patient123', 
                name: 'Александров А. А.', 
                role: 'patient' 
            }
        ];
        
        const user = users.find(u => u.email === email && u.password === password);
        
        if (user) {
            setAuthStatus(true, {
                name: user.name,
                role: user.role
            });
            
            closeModal();
            
            // Перенаправляем в зависимости от роли
            if (user.role === 'doctor') {
                window.location.href = 'vrach.html';
            } else {
                window.location.href = 'personal-account.html';
            }
        } else {
            throw new Error('Неверный email или пароль');
        }
    } catch (error) {
        alert(error.message);
        passwordInput.value = '';
        passwordInput.focus();
    }
}

// Инициализация событий
function initEventListeners() {
    // Открытие модального окна
    if (loginBtn) loginBtn.addEventListener('click', openModal);
    if (registerBtn) registerBtn.addEventListener('click', openModal);
    if (actionLoginBtn) actionLoginBtn.addEventListener('click', openModal);
    if (actionRegisterBtn) actionRegisterBtn.addEventListener('click', openModal);
    
    // Закрытие модального окна
    if (closeModalBtn) closeModalBtn.addEventListener('click', closeModal);
    
    // Обработка клика вне модального окна
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            closeModal();
        }
    });
    
    // Обработка нажатия Esc
    window.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && modal.style.display === 'block') {
            closeModal();
        }
    });
    
    // Обработка отправки формы
    if (submitBtn) {
        submitBtn.addEventListener('click', handleSubmit);
    }
    
    // Обработка нажатия Enter в полях ввода
    if (emailInput) {
        emailInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') handleSubmit(e);
        });
    }
    
    if (passwordInput) {
        passwordInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') handleSubmit(e);
        });
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    initEventListeners();
    
    // Если есть параметр ?login в URL, открываем модальное окно
    if (window.location.search.includes('login=true')) {
        openModal();
    }
});


