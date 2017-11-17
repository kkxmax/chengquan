//
//  FavouritesTableItemViewCell.h
//  chengxin
//
//  Created by common on 7/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MGSwipeTableCell.h"

@interface FavouritesTableItemViewCell : MGSwipeTableCell

@property (nonatomic, retain) IBOutlet UIImageView* photo;
@property (nonatomic, retain) IBOutlet UILabel* name;
@end
