var Fork = require('../../lib/github/fork');

describe("Fork", function() {

  function returnNoChildren() {
    var stub = sinon.stub();
    stub.callsArgWith(2, [], null);
    return {
      getForks: stub
    };
  }

  function returnWithChild(fork) {
    var stub = sinon.stub();
    stub.callsArgWith(2, [fork], null);
    return {
      getForks: stub
    };
  }

  it("should return an empty list of forks when there are no children", function(done) {
    var noChildRepo = new Fork('foo', 'bar');
    noChildRepo.client = returnNoChildren();

    noChildRepo.children(1, function(children, err) {
      expect(children.length).to.be.equal(0);
      done();
    });
  });

  it("should return a single fork when a repo has one child", function(done) {
    var singleChildRepo = new Fork('foo', 'bar');
    var childRepo = new Fork('baz', 'bar');

    singleChildRepo.client = returnWithChild(childRepo);
    childRepo.client = returnNoChildren();

    singleChildRepo.children(2, function(children, err) {
      expect(children).to.include.members([childRepo]);
      done();
    });

  });

  it("should return the children of forks", function(done) {
    var singleChildRepo = new Fork('foo', 'bar');
    var middleRepo = new Fork('baz', 'bar');
    var endRepo = new Fork('zap', 'bar');

    singleChildRepo.client = returnWithChild(middleRepo);
    middleRepo.client = returnWithChild(endRepo);
    endRepo.client = returnNoChildren();

    singleChildRepo.children(2, function(children, err) {
      var expectedChildren = [middleRepo, endRepo];
      expect(children).to.include.members(expectedChildren);
      done();
    });
  });

  it("should stop when it reaches max depth", function(done) {
      var singleChildRepo = new Fork('foo', 'bar');
      var middleRepo = new Fork('baz', 'bar');
      var endRepo = new Fork('zap', 'bar');

      singleChildRepo.client = returnWithChild(middleRepo);
      middleRepo.client = returnWithChild(endRepo);
      endRepo.client = returnNoChildren();

      singleChildRepo.children(1, function(children, err) {
        expect(children).to.include.members([middleRepo]);
        expect(children).not.to.include.members([endRepo]);
        done();
      });

    });

  it('should throw error when problem with retrieving forks', function(done) {
    var badRepo = new Fork('foo', 'bar');
    var badRepoError = new Error("Problem with fork");

    badRepo.client = {
      getForks: sinon.stub().callsArgWith(2, null, badRepoError)
    };

    badRepo.children(1, function(forks, err) {
      expect(err).to.be.equal(badRepoError);
      done();
    });
  });

});
