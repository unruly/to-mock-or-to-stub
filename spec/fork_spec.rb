require 'rspec'

describe 'GitHub forks' do
  it 'should do something' do
    fork = double("fork")
    expect(fork).to receive(:msg)
  end
end