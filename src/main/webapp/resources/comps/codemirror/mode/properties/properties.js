CodeMirror.defineMode("properties", function() {
  return {
    token: function(stream) {
      var ch = stream.next();
      stream.skipToEnd();
      if (ch == "#") return "comment";
    }
  };
});

CodeMirror.defineMIME("text/plain", "text");
