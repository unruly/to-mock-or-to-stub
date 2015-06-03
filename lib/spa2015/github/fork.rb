require_relative 'client'

module Spa2015
  module Github

    def client
      Spa2015::Github::Client.new
    end

    module_function :client

    class Fork

      @@max_depth = 3

      attr_reader :name, :child_count, :depth, :children

      def initialize(name, child_count, depth)
        @name = name
        @child_count = child_count
        @depth = depth
        @children = get_children
      end

      def get_children
        if @child_count > 0 and @depth < @@max_depth
          my_children = fetch_children
          their_children = my_children
                               .select { |child|
            child.child_count > 0
          }.each { |child|
            child.children
          }
          puts "#{name} has #{their_children.length} children!" if their_children.length > 0
          my_children.concat(their_children)
        else
          []
        end
      end

      def fetch_children
        puts "fetching #{@name} - depth: #{@depth}"
        Spa2015::Github.client.forks(@name).map { |fork|
          Fork.new(fork[:full_name], fork[:forks_count], @depth + 1)
        }
      end

      def self.parent(fork_name)
        repo = Spa2015::Github.client.repo(fork_name)
        Fork.new(repo[:full_name], repo[:forks_count], 0)
      end

    end
  end
end