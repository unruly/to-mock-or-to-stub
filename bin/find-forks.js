#!/usr/bin/env node

var Fork = require('../lib/github/fork'),
    HandlesBadForkWriter = require('../lib/report/handles_bad_fork_writer');

var user = process.argv[2];
var repo = process.argv[3];

new HandlesBadForkWriter(console).generateForkReport(new Fork(user, repo), 2);



