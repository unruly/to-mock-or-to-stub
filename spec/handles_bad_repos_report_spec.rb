require_relative '../lib/spa2015/report/handles_bad_fork_writer'

describe Spa2015::Report::HandlesBadForksReportWriter do

  before do
    @io = double('io')
    @writer = Spa2015::Report::HandlesBadForksReportWriter.new(@io)
  end

  it 'should print out the parent if it has not children' do
    expect_written(['user/name'])

    @writer.generate_fork_report(mock_fork('user/name'), 1)
  end


  it 'should print out the children of the parent' do
    expect_written(['user/name', 'child1/name', 'child2/name'])

    fork = mock_fork('user/name')
    allow(fork).to receive(:children).and_return([mock_fork('child1/name'), mock_fork('child2/name')])
    @writer.generate_fork_report(fork, 2)

  end

  it 'should fetch the children of children' do
    expect_written(['user/name', 'child/name', 'grandchild/name'])

    fork = mock_fork('user/name', [mock_fork('child/name', [mock_fork('grandchild/name')])])

    @writer.generate_fork_report(fork, 3)
  end

  it 'should report invalid repository if one fails' do
    expect_written(['user/name', '*** INVALID: bad/data', 'child2/name'])

    bad_fork = double('bad/data', :name => 'bad/data')
    allow(bad_fork).to receive(:children).and_raise('whoops')

    fork = mock_fork('user/name', [bad_fork, mock_fork('child2/name')])

    @writer.generate_fork_report(fork, 2)
  end

  def mock_fork(name, children = [])
    double(name, :name => name, :children => children)
  end

  def expect_written(strings)
    strings.each { |string|
      expect(@io).to receive(:puts).with(string)
    }
  end

end