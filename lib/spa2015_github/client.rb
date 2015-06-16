require 'octokit'

module Spa2015GitHub::Client

  @octokit_client = Octokit::Client.new

  module_function

  def get_forks(name)
    @octokit_client.forks(name)
  end
end