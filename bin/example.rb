#!/usr/bin/env ruby

require 'rubygems'

require_relative '../lib/spa2015'

parent = Spa2015::GitHub::Fork.new('guardian', 'sbt-jasmine-plugin')

writer = Spa2015::Report::HandlesBadForksReportWriter.new

writer.generate_fork_report(parent, 4)