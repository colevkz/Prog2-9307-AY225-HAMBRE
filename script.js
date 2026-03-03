let salesData = [];
let fileSelected = false;

document.getElementById('csvUpload').addEventListener('change', handleFileSelect);

function handleFileSelect(event) {
    const file = event.target.files[0];
    if (!file) return;

    fileSelected = true;

    const fileName = file.name;
    const fileSize = (file.size / 1024).toFixed(2) + ' KB';

    document.getElementById('fileName').textContent = 'File: ' + fileName;
    document.getElementById('fileSize').textContent = 'Size: ' + fileSize;

    document.querySelector('.upload-label').style.display = 'none';
    document.getElementById('fileInfo').classList.remove('hidden');

    const reader = new FileReader();
    reader.onload = function(e) {
        const text = e.target.result;
        parseCSV(text);
    };
    reader.readAsText(file);
}

function analyzeData() {
    if (!fileSelected || salesData.length === 0) {
        alert('Please select a valid CSV file first');
        return;
    }

    document.getElementById('uploadArea').classList.add('hidden');
    document.getElementById('loadingScreen').classList.remove('hidden');

    setTimeout(() => {
        document.getElementById('loadingScreen').classList.add('hidden');
        document.getElementById('menuNav').classList.remove('hidden');
        document.getElementById('content').classList.remove('hidden');
        
        updateAllViews();
        
        showNotification('Data analyzed successfully! ' + salesData.length + ' records loaded.');
    }, 2000);
}

function showNotification(message) {
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: linear-gradient(135deg, #ff1493, #ff69b4);
        color: white;
        padding: 20px 30px;
        border-radius: 10px;
        box-shadow: 0 10px 30px rgba(255, 20, 147, 0.4);
        z-index: 1000;
        font-weight: 600;
        animation: slideIn 0.5s ease;
    `;
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.animation = 'slideOut 0.5s ease';
        setTimeout(() => notification.remove(), 500);
    }, 3000);
}

function parseCSV(text) {
    const lines = text.split('\n');
    salesData = [];
    
    for (let i = 1; i < lines.length; i++) {
        if (!lines[i].trim()) continue;
        
        const values = parseCSVLine(lines[i]);
        
        if (values.length >= 7) {
            salesData.push({
                title: values[0].replace(/"/g, '').trim(),
                console: values[1].replace(/"/g, '').trim(),
                genre: values[2].replace(/"/g, '').trim(),
                publisher: values[3].replace(/"/g, '').trim(),
                totalSales: parseFloat(values[4].replace(/['"m]/g, '').trim()) || 0,
                releaseDate: values[5].replace(/"/g, '').trim(),
                lastUpdate: values[6].replace(/"/g, '').trim()
            });
        }
    }
}

function parseCSVLine(line) {
    const result = [];
    let current = '';
    let inQuotes = false;
    
    for (let i = 0; i < line.length; i++) {
        const char = line[i];
        
        if (char === '"') {
            inQuotes = !inQuotes;
        } else if (char === ',' && !inQuotes) {
            result.push(current);
            current = '';
        } else {
            current += char;
        }
    }
    result.push(current);
    return result;
}

function switchView(viewName) {
    document.querySelectorAll('.menu-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelector(`[data-view="${viewName}"]`).classList.add('active');
    
    document.querySelectorAll('.view').forEach(view => view.classList.remove('active'));
    document.getElementById(viewName).classList.add('active');
}

function updateAllViews() {
    updateSummary();
    updateMonthlySales();
    updateTopPublishers();
    updateGenreAnalysis();
}

function updateSummary() {
    const totalRecords = salesData.length;
    const totalSales = salesData.reduce((sum, game) => sum + game.totalSales, 0);
    const avgSales = totalSales / totalRecords;
    
    const uniqueConsoles = new Set(salesData.map(g => g.console)).size;
    const uniqueGenres = new Set(salesData.map(g => g.genre)).size;
    const uniquePublishers = new Set(salesData.map(g => g.publisher)).size;
    
    animateValue('totalRecords', 0, totalRecords, 1000);
    animateValue('totalSales', 0, totalSales, 1000, 'm');
    animateValue('avgSales', 0, avgSales, 1000, 'm');
    animateValue('uniqueConsoles', 0, uniqueConsoles, 1000);
    animateValue('uniqueGenres', 0, uniqueGenres, 1000);
    animateValue('uniquePublishers', 0, uniquePublishers, 1000);
    
    const topGame = salesData.reduce((max, game) => 
        game.totalSales > max.totalSales ? game : max, salesData[0]);
    
    document.getElementById('topGame').innerHTML = `
        <p><strong>Title:</strong> ${topGame.title}</p>
        <p><strong>Sales:</strong> ${topGame.totalSales.toFixed(2)}m</p>
        <p><strong>Console:</strong> ${topGame.console}</p>
        <p><strong>Genre:</strong> ${topGame.genre}</p>
    `;
}

function animateValue(id, start, end, duration, suffix = '') {
    const element = document.getElementById(id);
    const range = end - start;
    const increment = range / (duration / 16);
    let current = start;
    
    const timer = setInterval(() => {
        current += increment;
        if (current >= end) {
            current = end;
            clearInterval(timer);
        }
        
        if (suffix === 'm') {
            element.textContent = current.toFixed(2) + suffix;
        } else {
            element.textContent = Math.floor(current).toLocaleString();
        }
    }, 16);
}

function updateMonthlySales() {
    const monthlySales = {};
    
    salesData.forEach(game => {
        if (game.releaseDate) {
            const month = game.releaseDate.split(' ')[0];
            monthlySales[month] = (monthlySales[month] || 0) + game.totalSales;
        }
    });
    
    const sorted = Object.entries(monthlySales)
        .sort((a, b) => b[1] - a[1]);
    
    let html = `
        <table>
            <thead>
                <tr>
                    <th>Rank</th>
                    <th>Month</th>
                    <th>Total Sales</th>
                </tr>
            </thead>
            <tbody>
    `;
    
    sorted.forEach(([month, sales], index) => {
        html += `
            <tr>
                <td>${index + 1}</td>
                <td>${month}</td>
                <td>${sales.toFixed(2)}m</td>
            </tr>
        `;
    });
    
    html += `</tbody></table>`;
    document.getElementById('monthlyTable').innerHTML = html;
}

function updateTopPublishers() {
    const publisherData = {};
    
    salesData.forEach(game => {
        if (!publisherData[game.publisher]) {
            publisherData[game.publisher] = {
                sales: 0,
                count: 0
            };
        }
        publisherData[game.publisher].sales += game.totalSales;
        publisherData[game.publisher].count++;
    });
    
    const sorted = Object.entries(publisherData)
        .sort((a, b) => b[1].sales - a[1].sales)
        .slice(0, 10);
    
    let html = `
        <table>
            <thead>
                <tr>
                    <th>Rank</th>
                    <th>Publisher</th>
                    <th>Total Sales</th>
                    <th>Games</th>
                </tr>
            </thead>
            <tbody>
    `;
    
    sorted.forEach(([publisher, data], index) => {
        html += `
            <tr>
                <td>${index + 1}</td>
                <td>${publisher}</td>
                <td>${data.sales.toFixed(2)}m</td>
                <td>${data.count}</td>
            </tr>
        `;
    });
    
    html += `</tbody></table>`;
    document.getElementById('publishersTable').innerHTML = html;
}

function updateGenreAnalysis() {
    const genreData = {};
    
    salesData.forEach(game => {
        if (!genreData[game.genre]) {
            genreData[game.genre] = {
                sales: 0,
                count: 0
            };
        }
        genreData[game.genre].sales += game.totalSales;
        genreData[game.genre].count++;
    });
    
    const sorted = Object.entries(genreData)
        .sort((a, b) => b[1].sales - a[1].sales);
    
    let html = `
        <table>
            <thead>
                <tr>
                    <th>Rank</th>
                    <th>Genre</th>
                    <th>Total Sales</th>
                    <th>Games</th>
                    <th>Avg Sales</th>
                </tr>
            </thead>
            <tbody>
    `;
    
    sorted.forEach(([genre, data], index) => {
        const avgSales = data.sales / data.count;
        html += `
            <tr>
                <td>${index + 1}</td>
                <td>${genre}</td>
                <td>${data.sales.toFixed(2)}m</td>
                <td>${data.count}</td>
                <td>${avgSales.toFixed(2)}m</td>
            </tr>
        `;
    });
    
    html += `</tbody></table>`;
    document.getElementById('genreTable').innerHTML = html;
}

function exitDashboard() {
    if (confirm('Are you sure you want to exit the dashboard?')) {
        window.location.reload();
    }
}

const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);