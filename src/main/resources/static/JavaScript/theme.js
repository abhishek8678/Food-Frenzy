// Food Frenzy - Theme Management Engine (Dark Mode & Aesthetic White Mode)

(function initTheme() {
    try {
        const savedTheme = localStorage.getItem('ff_theme');
        if (savedTheme === 'light') {
            document.documentElement.setAttribute('data-theme', 'light');
        } else {
            document.documentElement.setAttribute('data-theme', 'dark');
        }
    } catch (e) {
        document.documentElement.setAttribute('data-theme', 'dark');
    }
})();

function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute('data-theme') || 'dark';
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    
    document.documentElement.setAttribute('data-theme', newTheme);
    try {
        localStorage.setItem('ff_theme', newTheme);
    } catch (e) {
        // Ignore localStorage restrictions if any
    }
    
    updateToggleButtons(newTheme);
}

function updateToggleButtons(theme) {
    const buttons = document.querySelectorAll('.theme-toggle-btn');
    const label = theme === 'dark' ? 'Switch to White Mode' : 'Switch to Dark Mode';
    buttons.forEach(btn => {
        btn.setAttribute('title', label);
        btn.setAttribute('aria-label', label);
    });
}

function setupThemeButtons() {
    const currentTheme = document.documentElement.getAttribute('data-theme') || 'dark';
    const buttons = document.querySelectorAll('.theme-toggle-btn');
    
    buttons.forEach(btn => {
        btn.removeEventListener('click', toggleTheme);
        btn.addEventListener('click', toggleTheme);
    });
    
    // For standalone views without a top navbar or header toggle, inject a floating toggle pill
    if (buttons.length === 0 && document.body) {
        const floatingBtn = document.createElement('button');
        floatingBtn.type = 'button';
        floatingBtn.className = 'theme-toggle-btn floating-theme-toggle';
        floatingBtn.innerHTML = '<i class="fa-solid fa-sun icon-sun"></i><i class="fa-solid fa-moon icon-moon"></i>';
        floatingBtn.addEventListener('click', toggleTheme);
        document.body.appendChild(floatingBtn);
    }
    
    updateToggleButtons(currentTheme);
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', setupThemeButtons);
} else {
    setupThemeButtons();
}
