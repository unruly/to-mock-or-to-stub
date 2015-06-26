var HandlesBadForkWriter = require('../../lib/report/handles_bad_fork_writer.js');
var Fork = require('../../lib/github/fork');

describe("HandlesBadForkWriter", function () {

    before(function() {
        sinon.spy(console, "log");
    });

    beforeEach(function() {
        console.log.reset();
    });

    it('should print a repo with no forks', function () {
        var singleRepoFork = new Fork("foo", "bar");
        singleRepoFork.children = sinon.stub().callsArgWith(1, [], null);

        var handlesBadForkWriter = new HandlesBadForkWriter(console);

        handlesBadForkWriter.generateForkReport(singleRepoFork, 1);

        expect(console.log).to.be.calledWith('foo/bar');
    });

    it('should print out forks', function () {
        var singleRepoFork = new Fork("foo", "bar");
        singleRepoFork.children = sinon.stub().callsArgWith(1, [new Fork("baz", "bar")], null);

        var handlesBadForkWriter = new HandlesBadForkWriter(console);

        handlesBadForkWriter.generateForkReport(singleRepoFork, 1);

        expect(console.log).to.be.calledWith('foo/bar');
        expect(console.log).to.be.calledWith('baz/bar');
    });

    it('should print out errors if it finds them', function () {
        var errorFork = new Fork("foo", "bar");
        errorFork.children = sinon.stub().callsArgWith(1, null, new Error("Bad fork"));

        var handlesBadForkWriter = new HandlesBadForkWriter(console);

        handlesBadForkWriter.generateForkReport(errorFork, 1);

        expect(console.log).to.be.calledWith("foo/bar");
        expect(console.log).to.be.calledWith("*** Error getting fork data");
    });

});
