window.onload = init;

bookId = '';

function init() {
  vm = new Vue({
    el: "#vue-container",
    data: {
       book: {}
    }
  });

  var urlParams = new URLSearchParams(window.location.search);
  bookId = urlParams.get('id');

  loadBook();

  document.querySelector('#nav-button-edit').onclick = () => { window.location = '/editbook.html?id=' + bookId} ;
  document.querySelector('#nav-button-del').onclick = () => { if (window.confirm('Действительно удалить?')) delBook(); } ;
  document.querySelector('#nav-button-home').onclick = () => { window.location = '/'} ;
}

function loadBook() {
  fetch("/book/" + bookId + "/")
      .then(response => response.json())
      .then(data => { vm.book = data });
}

function delBook() {
  fetch("/book/" + bookId + "/", {
    method: 'delete'
  }).then(response => { if (response.status == 204) window.location = '/' });
}

function loadComments() {
  fetch("/book/" + bookId + "/comment/")
      .then(response => response.json())
      .then(data => { vm.book.comments = data });
}

function addComment(comment) {
  fetch("/book/" + bookId + "/comment/",{
    method: 'post',
    headers: {
        'Accept': 'application/json, text/plain, */*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({reader: comment.userName, text: comment.commentText })
  }).then(loadComments);
}

function delComment(comment) {
  if (confirm('Действительно удалить?'))
    fetch("/book/" + bookId + "/comment/" + comment.id, {
        method: 'delete',
      }).then(loadComments);
}
