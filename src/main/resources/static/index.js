window.onload = init;

function init() {
  vm = new Vue({
    el: "#vue-container",
    data: {
       books: []
    }
  });

  getAll();

  document.querySelector('#nav-button-show-all').onclick = getAll;
  document.querySelector('#nav-button-new-book').onclick = () => { window.location = '/editbook.html'} ;
}

function getAll() {
  fetch("/book/")
      .then(response => response.json())
      .then(data => { vm.books = data });
}


function search(searchText) {
  if (searchText != "")
    fetch("/book/?filter=" + searchText)
      .then(response => response.json())
      .then(data => { vm.books = data });
}

