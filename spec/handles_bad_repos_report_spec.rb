require_relative '../lib/spa2015/report/handles_bad_fork_writer'
require_relative './stubs'

describe Spa2015::Report::HandlesBadForksReportWriter do

  before do
    @io = Spa2015::Stubs::StubIO.new
    @writer = Spa2015::Report::HandlesBadForksReportWriter.new(@io)
  end

  it 'should print out the parent if it has not children' do
    @writer.generate_fork_report(stub_fork('user/name'),1)
    expect_written('user/name')
  end


  it 'should print out the children of the parent' do
    stub = stub_fork('user/name').that_returns([stub_fork('child1/name'), stub_fork('child2/name')])
    @writer.generate_fork_report(stub,2)

    expect_written(['user/name', 'child1/name', 'child2/name'])
  end

  it 'should fetch the children of children' do
    grand_child = stub_fork('child2/name')
    child_stub = stub_fork('child1/name').that_returns([grand_child])
    stub = stub_fork('user/name').that_returns([child_stub])
    @writer.generate_fork_report(stub,3)

    expect_written(['user/name','child1/name','child2/name'])
  end

  it 'should report invalid repository if one fails' do
    stub = stub_fork('user/name').that_returns([Spa2015::Stubs::BrokenFork.new, stub_fork('child2/name')])

    @writer.generate_fork_report(stub,2)

    expect_written(['user/name', '*** INVALID: bad/data', 'child2/name'])  end

  def stub_fork(name)
    Spa2015::Stubs::StubFork.new(name)
  end

  def expect_written(strings)
    expect(@io.lines).to match_array(strings)
  end

end