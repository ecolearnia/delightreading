// const webpack = require("webpack")

module.exports = {
    env: {
        apiBaseUrl: process.env.API_BASE_URL || "http://localhost:8080"
    },
    srcDir: "web-ui/",
    /*
    ** Headers of the page
    */
    head: {
        title: "delightreading",
        meta: [
            { charset: "utf-8" },
            { name: "viewport", content: "width=device-width, initial-scale=1" },
            { hid: "description", name: "description", content: "DelightReading Web UI" }
        ],
        script: [
            { src: "https://code.jquery.com/jquery-3.2.1.min.js" }
        ],
        link: [
            { rel: "icon", type: "image/x-icon", href: "/favicon.ico" },
            { rel: "stylesheet", type: "text/css", href: "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css", crossorigin: "anonymous", integrity:"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"}
        ]
    },
    /*
    ** Customize the progress-bar color
    */
    loading: { color: "#3B8070" },
    /*
    ** Build configuration
    */
    build: {

        /*
        ** Run ESLINT on save
        extend(config, ctx) {
            if (ctx.dev && ctx.isClient) {
                config.module.rules.push({
                    enforce: "pre",
                    test: /\.(js|vue)$/,
                    loader: "eslint-loader",
                    exclude: /(node_modules)/
                })
            }
        }
        */
    }
}
