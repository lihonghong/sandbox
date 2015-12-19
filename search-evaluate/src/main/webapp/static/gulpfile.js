var gulp = require('gulp'),
    browserSync = require('browser-sync').create();

gulp.task('browser-sync', function() {
  browserSync.init({
    proxy: 'http://localhost:8080',
    files: ['**/*.html', 'css/*.css', '**/*.js', '!node_modules/']
  });
});

gulp.task('default', ['browser-sync']);
