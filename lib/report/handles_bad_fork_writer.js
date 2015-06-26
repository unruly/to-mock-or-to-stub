function HandlesBadForkWriter(writer) {
    this.writer = writer;
}

HandlesBadForkWriter.prototype.generateForkReport = function(fork, maxDepth) {
    var writer = this.writer;

    fork.children(maxDepth, function(forks, err) {
        writer.log(fork.id());

        if (err != null) {
            writer.log("*** Error getting fork data");
            return;
        }

        forks.forEach(function(childFork) {
            writer.log(childFork.id());
        })
    });

};

module.exports = HandlesBadForkWriter;