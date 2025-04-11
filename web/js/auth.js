document.addEventListener('DOMContentLoaded', function() {
    const protectedPages = ['personal-account.html', 'vrach.html', 'vrach1.html'];
    const currentPage = window.location.pathname.split('/').pop();
    
    if(protectedPages.includes(currentPage)) {
        if(localStorage.getItem('isAuthenticated') !== 'true') {
            window.location.href = 'pacient.html';
        } else {
            document.body.classList.add('authenticated');
        }
    }
});