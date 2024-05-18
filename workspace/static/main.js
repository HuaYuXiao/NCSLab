document.addEventListener('DOMContentLoaded', (event) => {
    function handleEnter(event) {
        var keyCode = event.which || event.keyCode;

        var input1 = document.getElementById('input1').value;
        if (keyCode === 13 && (input1 === 0 || input1 === 1)) {
            document.getElementById('inputForm').submit();
        } else if (keyCode === 13 && input1 !== '') {
            alert('Input for channel1 must be either 0 or 1.');
            event.preventDefault();
        }

        var input2 = document.getElementById('input2').value;
        if (keyCode === 13 && (input2 === 0 || input2 === 1)) {
            document.getElementById('inputForm').submit();
        } else if (keyCode === 13 && input2 !== '') {
            alert('Input for channel2 must be either 0 or 1.');
            event.preventDefault();
        }

        var input3 = document.getElementById('input3').value;
        if (keyCode === 13 && (input3 === 0 || input3 === 1)) {
            document.getElementById('inputForm').submit();
        } else if (keyCode === 13 && input3 !== '') {
            alert('Input for channel3 must be either 0 or 1.');
            event.preventDefault();
        }
    }

    // Add event listener to the input boxes for the Enter key
    document.getElementById('input1').addEventListener('keypress', handleEnter);
    document.getElementById('input2').addEventListener('keypress', handleEnter);
    document.getElementById('input3').addEventListener('keypress', handleEnter);
});
