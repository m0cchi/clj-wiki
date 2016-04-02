(function (){
    var borwserActions = {};
    var FOOT_BAR_MODE_AT_SHOW_VIEW = 'show-view';
    var subView = {};

    document.addEventListener('DOMContentLoaded', init, false);

    function init() {
        // attache event
        var headersAnchers = document.querySelectorAll('header > nav > ul > li > a');
        for(var i = 0; i < headersAnchers.length; i++) {
            headersAnchers[i].onclick = browserActionDispatcher;
        }
        // note button
        var showViewButton = document.getElementById('show-view-button');
        showViewButton.onclick = showView;
        // subview
        var subViews = document.querySelectorAll('.browser-action');
        for(var i = 0; 0 < subViews.length; i++){
            subView[subViews[i].getAttribute('id')] = subViews[i];
        }
    }

    function browserActionDispatcher() {
        var ancher = this.getAttribute('href');
        var fn = borwserActions[ancher];
        if(fn){
            fn();
        }
    }

    function showView() {
        var note = subView['note'];
        var footBar = subView['foot-bar'];
        var reopenButton = document.createElement('button');
        reopenButton.appendChild(document.createTextNode('exit'));
        // footBar
        footBar.innerHTML = '';//wip        
        note.style.display = 'none';
        footBar.dataset.mode = FOOT_BAR_MODE_AT_SHOW_VIEW;
        footBar.innerHTML = 'exit';
        footBar.onclick = function () {
            note.style.display = 'block';
            footBar.style.display = 'none';
        }

        footBar.style.display = 'block';
        
    }

    function openNoteViewer() {
        var note = subView['note'];
        note.style.display = 'block';
        titleEditingArea.focus();
    }

    borwserActions['#write'] = function() {
        openNoteViewer();
    };

    borwserActions['#logout'] = function() {
        location.href = '/logout';
    };

})();
