require_relative '../lib/spa2015/report/csv_writer'

class StubIO

  attr_reader :lines

  def initialize
    @lines = []
  end

  def puts(str)
    @lines << str
  end
end

class StubFork

  attr_accessor :name

  def initialize(name)
    @name = name
  end

  def that_returns(children)
    @my_children = children
    self
  end

  def children(_max_depth)
    @my_children || []
  end

end

class BrokenFork
  def children(_n)
    raise Octokit::Error
  end
end

describe Spa2015::Report::CsvWriter do

  before do
    @io = StubIO.new
    @writer = Spa2015::Report::CsvWriter.new(@io)
  end

  it 'should print out the parent if it has not children' do
    @writer.generate_fork_report(stub_fork('user/name'),1)
    expect_written('user/name')
  end


  it 'should print out the children of the parent' do
    stub = stub_fork('user/name').that_returns([stub_fork('child1/name'), stub_fork('child2/name')])
    @writer.generate_fork_report(stub,2)

    expect_written('user/name,child1/name,child2/name')
  end

  it 'should die if a fork raises an error' do
    stub = BrokenFork.new

    expect{@writer.generate_fork_report(stub, 1)}.to raise_error
  end

  def stub_fork(name)
    StubFork.new(name)
  end

  def expect_written(string)
    expect(@io.lines).to contain_exactly(string)
  end

end