Vue.component('book-short-view', {
  props: ['book', 'urlDetails'],
  template: `<article class="book-short-view">
              <header class="book-short-view__header-title"><a v-bind:href="urlDetails.replace('__id', book.id)">{{ book.title }}</a></header>
              <span class="book-short-view__item">{{ book.author.name }}</span>
              <span class="book-short-view__item">{{ book.genre }}</span><br/>
              <span class="book-short-view__item">{{ book.publisher }}</span>
              <span class="book-short-view__item">{{ book.year }}</span>
              <span class="book-short-view__item">ISBN: {{ book.isbn }}</span>
              </article>`
});

Vue.component('book-full-view', {
  props: ['book'],
  template: `<article class="book-full-view">
            <header class="book-full-view__header-title">{{ book.title }}</header>
            Автор: <span class="book-full-view__item">{{ book.author ? book.author.name : '' }}</span><br/>
            Жанр: <span class="book-full-view__item">{{ book.genre }}</span><br/>
            Издательство: <span class="book-full-view__item">{{ book.publisher }}</span><br/>
            Год издания: <span class="book-full-view__item">{{ book.year }}</span><br/>
            ISBN: <span class="book-full-view__item">{{ book.isbn }}</span>
            <header class="book-full-view__header_comments" v-if="book.comments && book.comments.length">Отзывы читателей ({{ book.comments.length }}):</header>
            <book-comment-view v-for="comment in book.comments" v-bind:key="comment.id" v-bind:comment="comment" v-on:del-comment="$emit('del-comment', $event)"></book-comment-view>
            </article>`
});

Vue.component('book-comment-view', {
  props: ['comment'],
  computed: {
    prettyDate: function() {
      var dt = new Date(this.comment.date);
      var fmt2 = (n) => { return ("" + (100 + n)).substring(1)};
      return `${dt.getHours()}:${fmt2(dt.getMinutes())} ${dt.getDate()}.${dt.getMonth()+ 1}.${dt.getFullYear()}`;
    }
  },
  template: `<article class="book-comment-view">
            <header class="book-comment-view__header"><span class="book-comment-view__reader"> {{ comment.reader }}</span> написал(а) в
            <span class="book-comment-view__date">{{ prettyDate }}</span>:
            <button class="book-comment-view__del-button flat-text-button flat-text-button--small" v-on:click="$emit('del-comment', comment)">Удалить</button>
            </header>
            <span class="book-comment-view__text">{{ comment.text }}</span>
            </article>`
});

Vue.component('add-comment-form', {
  data: function() {
    return {
             userName: '',
             commentText: ''
    }
  },
  methods: {
    newCommentClick: function() {
        this.$emit('new-comment', {userName: this.userName, commentText: this.commentText} );
        this.userName = '';
        this.commentText = '';
    }
  },
  template: `<form class="add-comment-form" v-on:submit.prevent="newCommentClick">
            <input type="text" v-model="userName" class="add-comment-form__name" placeholder="Ваше имя..." required="true"><br>
            <textarea v-model="commentText" class="add-comment-form__text" placeholder="Ваш отзыв..." required="true" rows="5"></textarea><br>
            <button class="add-comment-form__submit flat-text-button">Оставить отзыв</button>
            </form>`
});

Vue.component('search-form', {
  props: ['prompt'],
  data: function() {
    return {
       searchText: '',
    }
  },
  template: `<form class="search-form" v-on:submit.prevent="$emit('search', searchText)">
            <input type="text" v-model="searchText" class="search-form__text" v-bind:placeholder="prompt" required="true">
            <button class="search-form__submit flat-text-button">Искать</button>
            </form>`
});

Vue.component('edit-book-form', {
  props: ['book', 'authors', 'validationErrors'],
  computed: {
    flatGlobalErrors: function () {
      var s = '';
      if (this.validationErrors.global)
        this.validationErrors.global.forEach(e => { s += e; });
      return s;
    }
  },
  template: `<form class="edit-book-form" v-on:submit.prevent="$emit('save', book)">
            <div class="edit-book-form__row">
              <label class="edit-book-form__label" for="edit-book-form-title">Наименование: </label>
              <input id="edit-book-form-title" type="text" v-model="book.title" class="edit-book-form__input-title" required="true">
              <div id="edit-book-form-title" class="edit-book-form__input-validation-error validation-error-field">{{ validationErrors.fields ? validationErrors.fields.title : '' }}</div>
            </div>
            <div class="edit-book-form__row">
              <label class="edit-book-form__label" for="edit-book-form-author">Автор: </label>
              <input id="edit-book-form-author" list="edit-book-form-authors" type="search" v-model="book.author.name" class="edit-book-form__input-author" required="true">
              <datalist id="edit-book-form-authors">
                <option v-for="author in authors" v-bind:value="author.name" v-bind:key="author.id"></option>
              </datalist>
              <div id="edit-book-form-title" class="edit-book-form__input-validation-error validation-error-field">{{ validationErrors.fields ? validationErrors.fields.author : '' }}</div>
            </div>
            <div class="edit-book-form__row">
              <label class="edit-book-form__label" for="edit-book-form-genre">Жанр: </label>
              <input id="edit-book-form-genre" type="text" v-model="book.genre" class="edit-book-form__input-genre" required="true">
              <div id="edit-book-form-title" class="edit-book-form__input-validation-error validation-error-field">{{ validationErrors.fields ? validationErrors.fields.genre : '' }}</div>
            </div>
            <div class="edit-book-form__row">
              <label class="edit-book-form__label" for="edit-book-form-publisher">Издательство: </label>
              <input id="edit-book-form-publisher" type="text" v-model="book.publisher" class="edit-book-form__input-publisher" required="true">
              <div id="edit-book-form-title" class="edit-book-form__input-validation-error validation-error-field">{{ validationErrors.fields ? validationErrors.fields.publisher : '' }}</div>
            </div>
            <div class="edit-book-form__row">
              <label class="edit-book-form__label" for="edit-book-form-publisher">Год: </label>
              <input id="edit-book-form-year" type="number" v-model="book.year" class="edit-book-form__input-year" required="true" min="1800" v-bind:max="new Date().getFullYear()">
              <div id="edit-book-form-title" class="edit-book-form__input-validation-error validation-error-field">{{ validationErrors.fields ? validationErrors.fields.year : '' }}</div>
            </div>
            <div class="edit-book-form__row">
              <label class="edit-book-form__label" for="edit-book-form-isbn">ISBN: </label>
              <input id="edit-book-form-isbn" type="text" v-model="book.isbn" class="edit-book-form__input-isbn" required="true">
              <div id="edit-book-form-title" class="edit-book-form__input-validation-error validation-error-field">{{ validationErrors.fields ? validationErrors.fields.isbn : '' }}</div>
            </div>
            <div class="edit-book-form__row">
              <span class="edit-book-form__label"> </span>
              <span class="edit-book-form__input-empty"> </span>
              <div id="edit-book-form-title" class="edit-book-form__global-validation-error validation-error-global">{{ flatGlobalErrors }}</div>
            </div>
            <div class="edit-book-form__row">
              <span class="edit-book-form__label"> </span>
              <button class="edit-book-form__submit flat-text-button">Сохранить</button>
            </div>
            </form>`
});
