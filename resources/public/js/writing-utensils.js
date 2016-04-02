(function (){
    var borwserActions = {};

    document.addEventListener('DOMContentLoaded', init, false);

    function init() {
        // attache event
        var headersAnchers = document.querySelectorAll('header > nav > ul > li > a');
        for(var i = 0; i < headersAnchers.length; i++) {
            headersAnchers[i].onclick = browserActionDispatcher;
        }
    }

    function browserActionDispatcher() {
        var ancher = this.getAttribute('href');
        var fn = borwserActions[ancher];
        if(fn){
            fn();
        }
    }

    function openNoteViewer() {
        var note = document.getElementById('note');
        note.style.display = 'block';
        titleEditingArea.focus();
    }

    borwserActions['#write'] = function() {
        openNoteViewer();
    };

    borwserActions['#logout'] = function() {

    };

})();
