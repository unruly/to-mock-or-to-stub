function Fork(user, repo) {
    this.user = user;
    this.repo = repo;
    this.client = require('./client');
}

Fork.prototype.id = function() {
    return this.user + "/" + this.repo;
};

Fork.prototype.children = function(maxDepth, cb) {
    if (maxDepth <= 0) {
        cb([], null);
        return;
    }

    this.client.getForks(this.user, this.repo, function(forks, err) {

        var results = forks;

        if (err != null) {
            cb(null, err);
            return;
        }

        if (forks.length == 0) {
            cb([],null);
            return;
        }

        function makeCallback(fork) {
            return function (callback) {
                fork.children(maxDepth-1, function(newForks, err) {
                    callback(err, newForks);
                });
            }
        }

        var childForkGetters = [];

        for (var i = 0; i < forks.length; i++) {
            childForkGetters = childForkGetters.concat([makeCallback(forks[i])]);
        }

        require('async').parallel(childForkGetters,
            function(errors, outputs) {
                if (outputs.length > 0) {
                    results = results.concat.apply(results,outputs);
                }

                cb(results, null);
            }
        );
    })
};

module.exports = Fork;