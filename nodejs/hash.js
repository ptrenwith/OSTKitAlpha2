const crypto = require('crypto');

var buff = new Buffer.from('e894b8fc90b93c0bf24f760b711156fef925f12bf873bd036888d730b029ecec', 'utf8');
var hmac = crypto.createHmac('sha256', buff);
hmac.update(process.argv[2]);
console.log(hmac.digest('hex'));
