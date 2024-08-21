const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const developing = process.argv[process.argv.indexOf('--mode') + 1] === 'development';
const watching = process.argv[process.argv.indexOf('--watch')];

// Development
let devtool = false;
if (developing) devtool = 'source-map'

// Plugins
let configPlugins = [
  new HtmlWebpackPlugin({ // This helps create index.html in dist.
    template: './public/index.html'
  }),
];
if (watching) {
}

const config = { 
  context: __dirname,

  entry: {
    main: "./src/index.tsx"
  },

  output: {
    publicPath: '/',
    filename: '[name].js', // Output file name. The name is depended on entry.
    chunkFilename: '[name].js',
    library: { // Required for exporting custom library
      name: 'marci-ui/lib',
      type: 'umd2',
      umdNamedDefine: true
    },
  },

  optimization: {
    minimize: !developing,
  },

  module: {
    rules: [
      {
        test: /\.tsx?$/,
        loader: 'ts-loader',
        exclude: /node_modules/,
        // options: {
        //   transpileOnly: watching ? true : false
        // }
      },
      {
        test: /\.(css|s[ac]ss)$/i,
        use: [
          { loader: 'style-loader' },
          { loader: 'css-loader' },
          { loader: 'sass-loader', options: { sourceMap: developing } }
        ],
      }
    ]
  },

  resolve: {
    extensions: ['.ts', '.tsx', '.js'],
    modules: [path.resolve(__dirname, 'src'), 'node_modules']
  },

  watchOptions: {
    ignored: /node_modules/,
  },

  // Gotta external these, or else there will be 2 instant of React running
  externals: { 
    'react': 'react',
    'react-dom': 'react-dom',
    'bootstrap': 'bootstrap',
  }, 

  devtool: devtool,

  plugins: configPlugins,
} 

module.exports = config;