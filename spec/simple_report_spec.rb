require_relative '../lib/spa2015/report/csv_writer'
require_relative './stubs'

describe Spa2015::Report::CsvWriter do

  before do
    @io = Spa2015::Stubs::StubIO.new
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

  it 'should pass through max depth to children to get all their children' do
    stub = stub_fork('user/name')
    @writer.generate_fork_report(stub,2)

    expect(stub.max_depth).to be(2)
  end

  it 'should die if a fork raises an error' do
    stub = Spa2015::Stubs::BrokenFork.new

    expect{@writer.generate_fork_report(stub, 1)}.to raise_error
  end

  def stub_fork(name)
    Spa2015::Stubs::StubFork.new(name)
  end

  def expect_written(string)
    expect(@io.lines).to contain_exactly(string)
  end

end