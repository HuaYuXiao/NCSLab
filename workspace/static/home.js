function changeContent(contentName) {
    // You would fetch the new content here based on contentName
    // For demonstration, we'll just set some static text
    var newContent = '';
    switch (contentName) {
        case '3D Model':
            newContent = '<p>This is the 3D Model content.</p>';
            break;
        case 'Plant Information':
            newContent = '<p>This is the Plant Information content.</p>';
            break;
        case 'Control Algorithm':
            newContent = '<p>This is the Control Algorithm content.</p>';
            break;
        case 'Configuration':
            newContent = '<p>This is the Configuration content.</p>';
            break;
        case 'Algorithm Design':
            newContent = '<p>This is the Algorithm Design content.</p>';
            break;
        case 'Painting Area':
            newContent = '<p>This is the Painting Area content.</p>';
            break;
        default:
            newContent = '<p>default</p>';
    }
    document.getElementById('content').innerHTML = newContent;
}

// Set the default content to '3D Model' when the page loads
window.onload = function() {
    changeContent('3D Model');
};