require 'octokit'

module Spa2015::GitHub
  class Client

    def initialize
      @octokit_client = Octokit::Client.new
    end

    def get_forks(name)
      @octokit_client.forks(name)
    end

    def get_parent(name)
      @octokit_client.parent(name)
    end
  end
end