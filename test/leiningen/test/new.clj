(ns leiningen.test.new
  (:require [leiningen.new])
  (:use [clojure.test]
        [clojure.java.io :only [file]]
        [leiningen.test.helper :only [delete-file-recursively abort-msg]]))

(deftest test-new-with-just-project-name
  (leiningen.new/new nil "test-new-proj")
  (is (= #{"README.md" "project.clj" "src" "core.clj" "test"
           "doc" "intro.md" "test_new_proj" "core_test.clj" ".gitignore"}
         (set (map (memfn getName) (rest (file-seq (file "test-new-proj")))))))
  (delete-file-recursively (file "test-new-proj") :silently))

(deftest test-new-with-group-and-project-name
  (leiningen.new/new nil "orgname/a-project")
  (is (= #{"src" "a_project_test.clj" "project.clj" "a_project.clj" "orgname"
           "test" ".gitignore" "README.md" "doc" "intro.md"}
         (set (map (memfn getName)
                   (rest (file-seq (file "a-project")))))))
  (delete-file-recursively (file "a-project") :silently))

(deftest test-new-with-explicit-default-template
  (leiningen.new/new nil "default" "test-new-proj")
  (is (= #{"README.md" "project.clj" "src" "core.clj" "test"
           "doc" "intro.md" "test_new_proj" "core_test.clj" ".gitignore"}
         (set (map (memfn getName) (rest (file-seq (file "test-new-proj")))))))
  (delete-file-recursively (file "test-new-proj") :silently))

(deftest test-new-with-app-template
  (leiningen.new/new nil "app" "test-new-app")
  (is (= #{"README.md" "project.clj" "src" "core.clj" "test"
           "doc" "intro.md" "test_new_app" "core_test.clj" ".gitignore"}
         (set (map (memfn getName) (rest (file-seq (file "test-new-app")))))))
  (delete-file-recursively (file "test-new-app") :silently))

(deftest test-new-with-plugin-template
  (leiningen.new/new nil "plugin" "test-new-plugin")
  (is (= #{"README.md" "project.clj" "src" "leiningen" "test_new_plugin.clj" ".gitignore"}
         (set (map (memfn getName) (rest (file-seq (file "test-new-plugin")))))))
  (delete-file-recursively (file "test-new-plugin") :silently))

(deftest test-new-with-template-template
  (leiningen.new/new nil "template" "test-new-template")
  (is (= #{"README.md" "project.clj" "src" "leiningen" "new"
           "test_new_template.clj" "test_new_template" "foo.clj" ".gitignore"}
         (set (map (memfn getName) (rest (file-seq (file "test-new-template")))))))
  (delete-file-recursively (file "test-new-template") :silently))

(deftest test-new-with-nonexistent-template
  (is (re-find
       #"^Could not find template zzz"
       (with-redefs [leiningen.new/resolve-remote-template (constantly false)]
         (abort-msg leiningen.new/new nil "zzz" "my-zzz")))))

(deftest test-new-with-show-describes-a-template
  (is (re-find
       #"^A general project template for libraries"
       (with-out-str
         (leiningen.new/new nil "default" ":show")))))

(deftest test-new-with-to-dir-option
  (leiningen.new/new nil "test-new-proj" "--to-dir" "my-proj")
  (is (= #{"README.md" "project.clj" "src" "core.clj" "test"
           "doc" "intro.md" "test_new_proj" "core_test.clj" ".gitignore"}
         (set (map (memfn getName) (rest (file-seq (file "my-proj")))))))
  (delete-file-recursively (file "my-proj") :silently))