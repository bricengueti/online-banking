// Base auth
db = db.getSiblingDB('auth_db_test');
db.createCollection('init');

// Base accounting
db = db.getSiblingDB('accounting_db_test');
db.createCollection('init');