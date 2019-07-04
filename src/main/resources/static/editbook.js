window.onload = init;

bookId = '';

function init() {
  vm = new Vue({
    el: "#vue-container",
    data: {
       book: {author: {}},
       authors: [],
       validationErrors: {}
    }
  });

  var urlParams = new URLSearchParams(window.location.search);
  bookId = urlParams.get('id');

  if (bookId)
    loadBook();

  loadAuthors();

  document.querySelector('.header-of-page').innerHTML = (bookId ? 'Изменить' : 'Добавить') + ' книгу';
}

function loadBook() {
  fetch("/book/" + bookId + "/")
      .then(response => response.json())
      .then(data => { vm.book = data });
}

function loadAuthors() {
  fetch("/author/")
      .then(response => response.json())
      .then(data => { vm.authors = data });
}

function saveBook(book) {
  if (bookId)
    updateBook(book);
  else
    createBook(book);
}

function updateBook(book) {
  fetch("/book/" + bookId + "/",{
      method: 'put',
      headers: {
          'Accept': 'application/json, text/plain, */*',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)
    }).then(response => updated(response) );
}

function createBook(book) {
  fetch("/book/",{
      method: 'post',
      headers: {
          'Accept': 'application/json, text/plain, */*',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)
    }).then(response => updated(response) );
}

function updated(response) {
  if (response.status == 201 /*created*/) {
    bookId = response.headers.get('Location').split('/')[2];
    window.location = 'viewbook.html?id=' + bookId;
  } else if (response.status == 200 /*ok*/) {
    window.location = 'viewbook.html?id=' + bookId;
  } else if (response.status == 400) /*bad request*/ {
    response.json().then(data => { vm.validationErrors = data } );
  }
}
