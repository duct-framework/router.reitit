# Duct Reitit Router [![Build Status](https://github.com/duct-framework/router.reitit/actions/workflows/test.yml/badge.svg)](https://github.com/duct-framework/router.reitit/actions/workflows/test.yml)

A router [Duct][] framework that uses [Reitit][]. This library is also
compatible with [Integrant][].

[duct]: https://github.com/duct-framework/duct
[reitit]: https://github.com/metosin/reitit
[integrant]: https://github.com/weavejester/integrant

## Installation

Add the following dependency to your deps.edn file:

    org.duct-framework/router.reitit {:mvn/version "0.4.0"}

Or to your Leiningen project file:

    [org.duct-framework/router.reitit "0.4.0"]

## Usage

Add the `:duct.router/reitit` key to your Duct (or Integrant)
configuration:

```edn
{:duct.router/reitit
 {:routes ["/" {:get #ig/ref :example.handler/root}]}
 :example.handler/root {}}
```

Once initiated the `:duct.router/reitit` key will produce a Ring
handler.

There are four top level options available:

- `:routes` - the Reitit routing data
- `:middleware` - a vector of middleware to apply to the Ring handler
- `:data` - data to add to every Reitit route
- `:handlers` - a vector of handlers to fall back

The `:data` key takes a map and acts as it does in Reitit, except for
the following keys:

- `:muuntaja` - a map of Muuntaja options to be merged with the defaults
- `:coercion` - one of: `:malli`, `:schema` or `:spec`

These keys will automatically add relevant middleware.

The `:duct.handler.reitit/default` key will initiate into a handler
using the Reitit `create-default-handler` function. It takes the
following options:

- `:not-found` - a handler for 404 HTTP errors
- `:method-not-allowed` - a handler for 405 HTTP errors
- `:not-acceptable` - a handler for 406 HTTP errors

This can be referenced in the `:handlers` vector to provide a default
handler:

```edn
{:duct.router/reitit
 {:routes   ["/" {:get #ig/ref :example.handler/root}]
  :handlers [#ig/ref :duct.handler.reitit/default]}
 :duct.handler.reitit/default {}
 :example.handler/root {}}
```

## License

Copyright © 2025 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
