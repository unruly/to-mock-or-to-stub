module Spa2015::Stubs
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
    attr_accessor :name

    def initialize
      @name = 'bad/data'
    end

    def children(_n)
      raise Octokit::Error
    end
  end

end