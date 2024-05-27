function mostrarPopup() {
  document.getElementById("miPopup").style.display = "block";
}

function cerrarPopup() {
  document.getElementById("miPopup").style.display = "none";
}

// Ejemplo de asociación de eventos
//document.getElementById('botonAbrir').addEventListener('click', mostrarPopup);
document.getElementById("botonCerrar").addEventListener("click", cerrarPopup);
