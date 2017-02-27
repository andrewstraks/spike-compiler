'import $rest as app.rest';
app.service.register("Comments", {

    cachedComments: null,

    getComments: function (postId) {

        var someData = 'name is $[[postId]]';
        someData += "name is $[postId]";
        someData += 'name is "$[[postId]]"';

        return $rest.get(this.cachedComments || app.config.apiUrl + '/comments', {
            urlParams: {
                postId: postId
            }
        });

    }
});
SPIKE_IMPORT_END

'import $footer as app.component.Footer';
'import $postsList as app.component.PostsList';

app.controller.register("Home", {

    components: {
        PostsList: {
            recentPosts: true
        }
    },

    init: function (params) {

        var person = {
            name: 'Dawid',
            surname: 'Senko',
            nick: 'Sęp'
        };

        var gstring = 'Person name: ${{person.name}}';
        gstring += ", surname: ${person.surname}";
        gstring = ', nick: "${{person.nick}}" ';

        $this.selector.home().click(function(){
            app.router.redirect(app.router.createLink('/someLink'))
        });

    },

    dupa: function(){
        $footer.ok();
    }

});
SPIKE_IMPORT_END
app.component.register("Menu", {

    init: function () {

        $this.selector.home().click(function(){
            app.router.redirect(app.router.createLink('/someLink'))
        });

    },

});
SPIKE_IMPORT_END

'import $postService as app.service.Post';
app.abstract.register("TestAbstract", {

    createRecentPostsList: function () {

        $postService.getRecentPosts()
            .then(function (posts) {
                $super.createPostsList(posts, 5);
            })
            .catch(function (error) {
            });

    },

    createAllPostsList: function (arg1, arg2) {

        $postService.getPosts()
            .then(function (posts) {
                $super.createPostsList(posts, 20);
            })
            .catch(function (error) {
            });

        return {

            name: 'Peter',

            getName: function(){
                app.log('name');
            }

        }
    },



});

SPIKE_IMPORT_END
'import $postService as app.service.Post';
'import $postsList as app.component.PostList';

app.component.register("PostsList", {

    inherits: [
        app.abstract.TestAbstract
    ],

    init: function (data) {

        if (data.recentPosts) {
            $this.createRecentPostsList();
        } else {
            $this.createAllPostsList();
        }

    },

    createPostsList: function (posts, limit) {

        app.lister.PostsList.render(
            $postsList.selector.postsList(),
            posts,
            {
                select: app.component.PostsList.selectPost
            },
            {
                limit: limit
            }
        );

    },

    selectPost: function (e) {

        app.router.redirect('post/:postId', {
            postId: e.eCtx.id
        });

    }

});

SPIKE_IMPORT_END
