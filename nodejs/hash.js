const crypto = require('crypto');

var buff = new Buffer.from('', 'utf8');
var hmac = crypto.createHmac('sha256', buff);
hmac.update(process.argv[2]);
console.log(hmac.digest('hex'));
