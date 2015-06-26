var HandlesBadForkWriter = require('../../lib/report/handles_bad_fork_writer.js');
var Fork = require('../../lib/github/fork');

describe("HandlesBadForkWriter", function () {

    function newStubWriter() {
        return {
            data: [],
            log: function (input) {
                this.data = this.data.concat(input);
            }
        };
    }

    it('should print a repo with no forks', function () {
        var singleRepoFork = new Fork("foo", "bar");
        singleRepoFork.children = function (maxDepth, callback) {
            callback([], null);
        };

        var stubWriter = newStubWriter();
        var handlesBadForkWriter = new HandlesBadForkWriter(stubWriter);

        handlesBadForkWriter.generateForkReport(singleRepoFork, 1);

        expect(stubWriter.data).to.include.members(['foo/bar']);

    });

    it('should print out forks', function () {
        var singleRepoFork = new Fork("foo", "bar");
        singleRepoFork.children = function (maxDepth, callback) {
            callback([new Fork("baz", "bar")], null);
        };

        var stubWriter = newStubWriter();
        var handlesBadForkWriter = new HandlesBadForkWriter(stubWriter);

        handlesBadForkWriter.generateForkReport(singleRepoFork, 1);

        expect(stubWriter.data).to.include.members(['foo/bar','baz/bar']);
    });

    it('should print out errors if it finds them', function () {
        var errorFork = new Fork("foo", "bar");
        errorFork.children = function (maxDepth, callback) {
            callback(null, new Error("Bad fork"));
        };

        var stubWriter = newStubWriter();
        var handlesBadForkWriter = new HandlesBadForkWriter(stubWriter);

        handlesBadForkWriter.generateForkReport(errorFork, 1);

        expect(stubWriter.data).to.include.members(['foo/bar','*** Error getting fork data']);
    });

});
