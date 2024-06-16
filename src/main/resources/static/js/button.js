const updateButton = document.getElementsByClassName("updateButton");
const closeButton = document.getElementsByClassName("closeButton");
const dialog = document.getElementById("updateModal");
dialog.returnValue = "favAnimal";


function openCheck(dialog) {
    if (dialog.open) {
        console.log("Dialog open");
    } else {
        console.log("Dialog closed");
    }
}

// Update button opens a modal dialog
updateButton.addEventListener("click", () => {
    const id = $(this).closest("tr").find("td:first").text();
    fetchPerson(id);
    dialog.showModal();
    openCheck(dialog);
});

// Form cancel button closes the dialog box
closeButton.addEventListener("click", () => {
    dialog.close("No Updates");
    openCheck(dialog);
});