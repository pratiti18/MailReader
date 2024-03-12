console.log("Inside table js");
function exportToCsv() {
        // Make a GET request to your backend API endpoint
        console.log("Inside table js");
        fetch('/readMails/generate')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to generate CSV file');
                }
                return response.blob();
            })
            .then(blob => {
                // Create a temporary anchor element
                const url = window.URL.createObjectURL(new Blob([blob]));
                const a = document.createElement('a');
                a.href = url;
                a.download = 'mailData.csv';
                // Trigger a click event to start the download
                a.click();
                // Clean up
                window.URL.revokeObjectURL(url);
            })
            .catch(error => {
                console.error('Export to CSV failed:', error);
            });
}

function myFunctionNames() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("myTable");
    tr = table.getElementsByTagName("tr");
  
    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[1];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }

  function myFunctionProjects() {
    // Declare variables
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("myInput1");
    filter = input.value.toUpperCase();
    table = document.getElementById("myTable");
    tr = table.getElementsByTagName("tr");
  
    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[4];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
        } else {
          tr[i].style.display = "none";
        }
      }
    }
  }

  function dropdownfunc() {
    document.getElementById("myDropdown").classList.toggle("show");
  }
  
  // Close the dropdown menu if the user clicks outside of it
  window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {
      var dropdowns = document.getElementsByClassName("dropdown-content");
      var i;
      for (i = 0; i < dropdowns.length; i++) {
        var openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
          openDropdown.classList.remove('show');
        }
      }
    }
  }

  function showSearchNames() {
    document.getElementById("commonSearchName").style.display = 'block';
    document.getElementById("commonSearchProject").style.display='none';
}
function showSearchProjects() {
    document.getElementById("commonSearchProject").style.display = 'block';
    document.getElementById("commonSearchName").style.display='none';
}


