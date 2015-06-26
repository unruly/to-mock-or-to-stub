var GitHubApi = require('github');
var github = new GitHubApi({
    version: "3.0.0",
    debug: false,
    protocol: "https"
});

exports.getForks = function(user, repo, callback) {
    var Fork = require('./fork');
    github.repos.getForks({
        user: user,
        repo: repo
    }, function(err, results) {

        if (err != null) {
            callback([], err);
            return;
        }

        callback(results.map(function(obj) {
            return new Fork(obj.owner.login, obj.name);
        }))
    });
};