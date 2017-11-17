//
//  ProductTableViewCell.h
//  chengxin
//
//  Created by common on 7/31/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ProductTableViewCell : UITableViewCell

@property (nonatomic, retain) IBOutlet UIImageView* photo;
@property (nonatomic, retain) IBOutlet UILabel* name;
@property (nonatomic, retain) IBOutlet UILabel* price;

@end
