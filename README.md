# Duct Reitit Router [![Build Status](https://github.com/duct-framework/router.reitit/actions/workflows/test.yml/badge.svg)](https://github.com/duct-framework/router.reitit/actions/workflows/test.yml)

A router [Duct][] framework that uses [Reitit][]. This library is also
compatible with [Integrant][].

[duct]: https://github.com/duct-framework/duct
[reitit]: https://github.com/metosin/reitit
[integrant]: https://github.com/weavejester/integrant

## Installation

Add the following dependency to your deps.edn file:

    org.duct-framework/router.reitit {:mvn/version "0.1.0-SNAPSHOT"}

Or to your Leiningen project file:

    [org.duct-framework/router.reitit "0.1.0-SNAPSHOT"]

## Usage

Add the `:duct.router/reitit` key to your Duct (or Integrant)
configuration:

```edn
{:duct.router/reitit
 {:routes ["/" {:get #ig/ref :example.handler/root}]}
 :example.handler/root {}}
```

The `:routes` option defines the Reitit routes. Other options are
passed directly to the Reitit router.

Once initiated the `:duct.router/reitit` key will produce a Ring
handler.

## License

Copyright © 2024 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
