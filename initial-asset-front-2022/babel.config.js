module.exports = {
    presets: [
        ['@vue/app', {
            'useBuiltIns': 'entry'
        }],
        // [
        //     "env", {
        //         "modules": false,
        //         "useBuiltIns": "entry"
        //     }
        // ],
        // "stage-3"
    ],
    plugins: [
        '@babel/plugin-proposal-optional-chaining'
    ]
}
