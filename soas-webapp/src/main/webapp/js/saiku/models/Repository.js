/*
 * Repository.js
 * 
 * Copyright (c) 2011, OSBI Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
/**
 * Repository query
 */

var RepositoryObject = Backbone.Model.extend( {
    url: function( ) {
        var segment = Settings.BIPLUGIN ? 
            "/pentahorepository2/resource" : "/repository2/resource";
        return encodeURI(Saiku.session.username + segment);
    }
} );

var SavedQuery = Backbone.Model.extend({

    parse: function(response) {
        //console.log("response: " + response);
        //this.xml = response;
    },
    
    url: function() {
        var u = Settings.BIPLUGIN ? 
                encodeURI(Saiku.session.username + "/pentahorepository2/resource")  
                    : encodeURI(Saiku.session.username + "/repository2/resource");
        return u;
    },
    
    move_query_to_workspace: function(model, response) {
        var file = response;
        var filename = model.get(/*'file'*/'name');//TODO 改动这里使标题栏显示名称，看是否有bug
        for (var key in Settings) {
            if (key.match("^PARAM")=="PARAM") {
                var variable = key.substring("PARAM".length, key.length)
                file = file.replace("${" + variable + "}", Settings[key]);
            }
        }
        var query = new Query({ 
            xml: file,
            formatter: Settings.CELLSET_FORMATTER
        },{
            name: filename
        });
        
        var tab = Saiku.tabs.add(new Workspace({ query: query }));
    }
});

/**
 * Repository adapter
 */
var Repository = Backbone.Collection.extend({
    model: SavedQuery,
    
    initialize: function(args, options) {
        this.dialog = options.dialog;
    },
    
    parse: function(response) {
        this.dialog.populate(response);
    },
    
    url: function() {
        var segment = Settings.BIPLUGIN ? 
            "/pentahorepository2/?type=saiku" : "/repository2/?type=saiku";
        return encodeURI(Saiku.session.username + segment);
    }
});
