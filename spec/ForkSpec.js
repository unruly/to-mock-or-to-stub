describe("Fork", function() {

  it("should have a working test framework", function() {
      var callback = sinon.spy();
      expect(callback).to.have.been.calledWith('bar');
  });

});
