"use strict";

const abstractOperation = f => (...args) => (...values) => f(...args.map(arg => arg(...values)));
const cnst = value => () => value;
// :NOTE: Неэффективно
const variable = name => (...values) => values[variables.indexOf(name)];
const negate = abstractOperation(x => -x);
const add = abstractOperation((x, y) => x + y);
const subtract = abstractOperation((x, y) => x - y);
const multiply = abstractOperation((x, y) => x * y);
const divide = abstractOperation((x, y) => x / y);
const min5 = abstractOperation((...args) => Math.min(...args));
const max3 = abstractOperation((...args) => Math.max(...args));


const one = cnst(1);
const two = cnst(2);

const operations = {
    "min5": [5, min5],
    "max3": [3, max3],
    "+": [2, add],
    "-": [2, subtract],
    "*": [2, multiply],
    "/": [2, divide],
    "negate": [1, negate],
    getArity: function (op) {
        return this[op][0]
    },
    getOperation: function (op) {
        return this[op][1]
    }
};

const variables = ["x", "y", "z"];

const constants = {
    "one": one,
    "two": two,
    getConst: function (cnst) {
        return this[cnst];
    }
};

const parse = expression => {
    let expr = [];
    for (const curr of expression.split(" ").filter(str => str.length !== 0)) {
        if (curr in operations) {
            expr.push(operations.getOperation(curr)(...expr.splice(-operations.getArity(curr))));
        } else if (variables.includes(curr)) {
            expr.push(variable(curr));
        } else if (curr in constants) {
            expr.push(constants.getConst(curr));
        } else {
            expr.push(cnst(Number.parseInt(curr)));
        }
    }
    return expr.pop();
}

const ex = add(
    subtract(
        multiply(variable("x"), variable("x")),
        multiply(cnst(2), variable("x"))
    ),
    cnst(1)
);

for (let i = 0; i < 11; i++) {
    println(ex(i))
}
