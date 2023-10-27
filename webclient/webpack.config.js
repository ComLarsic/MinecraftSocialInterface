const path = require('path');

module.exports = {
    entry: './src/app.ts',
    mode: 'development',
    module: {
        rules: [{
            test: /\.ts?$/,
            use: 'ts-loader',
            exclude: /node_modules/
        }]
    },
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist/')
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.jsx', '.js']
    },
}