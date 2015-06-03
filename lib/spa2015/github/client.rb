require 'rubygems'
require 'octokit'

module Spa2015
  module Github
    class Client
      def initialize

      end

      def repo(name)
        {
            :full_name => 'foo',
            :forks_count => 0
        }
      end
    end
  end
end